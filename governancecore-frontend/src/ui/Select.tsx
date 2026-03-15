import "./Select.css";

// This is a Select primitive UI which will be reused throughout the project.

type Props = React.SelectHTMLAttributes<HTMLSelectElement>;

export default function Select(props: Props) {
  return <select className="ui-select" {...props} />;
}